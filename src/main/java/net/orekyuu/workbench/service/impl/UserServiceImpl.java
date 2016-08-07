package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.entity.UserAvatar;
import net.orekyuu.workbench.entity.dao.UserAvatarDao;
import net.orekyuu.workbench.entity.dao.UserDao;
import net.orekyuu.workbench.service.UserService;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Transactional(readOnly = true)
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserAvatarDao userAvatarDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = false)
    @Override
    public void createUser(String id, String name, String rawPassword, boolean admin) throws UserExistsException {
        User user = new User(id, name, passwordEncoder.encode(rawPassword), admin);
        try {
            userDao.insert(user);
        } catch (DuplicateKeyException e) {
            //ユーザーが存在してるので失敗
            throw new UserExistsException(id, e);
        }

        UserAvatar avatar = new UserAvatar();
        avatar.id = user.id;
        try(InputStream in = getClass().getClassLoader().getResourceAsStream("default-user-icon.png")) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            FileCopyUtils.copy(in, outputStream);
            avatar.avatar = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            userAvatarDao.insert(avatar);
        } catch (DuplicateKeyException e) {
            //ユーザーが存在してるので失敗
            throw new UserExistsException(id, e);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<byte[]> findAvatar(String userId) {
        return userAvatarDao.findById(userId).map(a -> a.avatar);
    }

    @Transactional(readOnly = false)
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Transactional(readOnly = false)
    @Override
    public void updateIcon(UserAvatar avatar) {
        userAvatarDao.update(avatar);
    }
}
