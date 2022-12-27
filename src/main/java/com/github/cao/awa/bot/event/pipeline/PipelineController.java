package com.github.cao.awa.bot.event.pipeline;

public class PipelineController {
    private boolean canceled = false;

    public void cancel() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return this.canceled;
    }
}
