export class FeatureGroupEntity {
    id!: string
    name: string
    dimensions: string
    constructor(name: string,dimensions: string,id?:string) {
        this.name = name
        this.dimensions = dimensions
        if(id){
            this.id = id
        }
    }

}