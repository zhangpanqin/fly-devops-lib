package com.mflyyou

class PipelineRetry {
    def int timeoutSeconds
    def int maxAttempts
    def script

    PipelineRetry(script, int maxAttempts, int timeoutSeconds) {
        this.script = script
        this.timeoutSeconds = timeoutSeconds + 15
        this.maxAttempts = maxAttempts
    }

    public void retryOrAbort(final Closure<?> action, final int count = 0) {
        script.echo "Attempting action; attempt count is: ${count}"
        try {
            action.call();
        } catch (final exception) {
            script.echo "${exception.toString()}"
            script.timeout(time: timeoutSeconds, unit: 'SECONDS') {
                count++
                if (count < maxAttempts) {
                    script.echo "Retrying from failed stage."
                    script.sleep(time: 15, unit: "SECONDS")
                    return retryOrAbort(action, count)
                } else {
                    script.echo "Max attempts reached. Ending stage without retry."
                    throw exception
                }
            }
        }
    }
}