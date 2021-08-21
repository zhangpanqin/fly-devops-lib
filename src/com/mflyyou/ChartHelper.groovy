package com.mflyyou

class ChartHelper implements Serializable {
    def script

    ChartHelper(script) {
        this.script = script
    }

    def overrideChartInfo(String serviceName) {
        def chartContent = """
                            apiVersion: v1
                            appVersion: "1.0"
                            description: A Helm chart for ${serviceName}
                            name: ${serviceName}
                            version: 1.0.0
                            """
        script.writeFile file: "charts/Chart.yaml", text: chartContent
    }
}
