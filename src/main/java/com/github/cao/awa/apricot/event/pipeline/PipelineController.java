package com.github.cao.awa.apricot.event.pipeline;

public class PipelineController {
    private boolean canceled = false;

    public void cancel() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return this.canceled;
    }
}
