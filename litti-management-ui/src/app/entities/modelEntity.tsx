export class ModelEntity {
    name: String
    version: String
    domain: String
    framework: String
    outputs: String
    constructor(name: String,version: String,domain: String,framework: String,outputs: String) {
        this.name = name
        this.version = version
        this.domain = domain
        this.framework = framework
        this.outputs = outputs
    }

    toString() {
        return this.name;
    }

}