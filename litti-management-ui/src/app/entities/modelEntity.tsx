export class ModelEntity {
    id!: string
    name: string
    version: string
    domain: string
    modelFramework: string
    modelLocation: string
    outputs: string
    constructor(name: string,version: string,domain: string,framework: string,modelLocation:string,outputs: string, id?:string) {
        this.name = name
        this.version = version
        this.domain = domain
        this.modelFramework = framework
        this.modelLocation = modelLocation
        this.outputs = outputs
        if(id){
            this.id = id
        }
    }

}