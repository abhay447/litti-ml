import { ArtifactEntity } from "./artifactEntity"

export class ModelEntity {
    id!: string
    name: string
    version: string
    domain: string
    modelFramework: string
    outputs: string
    modelArtifact!: ArtifactEntity
    modelArtifactId!: string
    constructor(name: string, version: string, domain: string, framework: string, outputs: string, id?: string, artifactEntity?: ArtifactEntity) {
        this.name = name
        this.version = version
        this.domain = domain
        this.modelFramework = framework
        this.outputs = outputs
        if (id) {
            this.id = id
        }
        if (artifactEntity) {
            this.modelArtifact = artifactEntity
            this.modelArtifactId = artifactEntity?.id
        }
    }

}