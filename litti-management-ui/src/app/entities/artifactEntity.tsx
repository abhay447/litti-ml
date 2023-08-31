export class ArtifactEntity{
    id!: string
    storageLocation!:string
    storageType!:string

    constructor(id?: string, storageLocation?: string, storageType?: string) {
        if (id) {
            this.id = id
        }
        if (storageLocation) {
            this.storageLocation = storageLocation
        }
        if (storageType) {
            this.storageType = storageType
        }
    }
}