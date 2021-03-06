package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.message.LogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SaveArtifactTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(SaveArtifactTask.class);

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        logger.info("Save Start");
        Thread.sleep(100);
        messenger.send(new LogMessage("Saved!"));
        logger.info("Save End");
        return true;
    }
}
