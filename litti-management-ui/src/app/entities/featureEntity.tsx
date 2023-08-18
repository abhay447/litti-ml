export class FeatureEntity {
    id!: string
    name: string
    version: string
    dataType: string
    defaultValue: string
    featureGroupId: string
    ttlSeconds: number
    constructor(name: string,version: string,dataType: string,defaultValue: string,featureGroupId: string,ttlSeconds:number, id?:string) {
        this.name = name
        this.version = version
        this.dataType = dataType
        this.defaultValue = defaultValue
        this.featureGroupId = featureGroupId
        this.ttlSeconds = ttlSeconds;
        if(id){
            this.id = id
        }
    }

}