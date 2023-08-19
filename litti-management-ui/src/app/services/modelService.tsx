import { ModelEntity } from "@/app/entities/modelEntity"

export  async function getModels(){
    const res = await fetch('http://localhost:8081/models');
  const data = await res.json();
  const models = (data as Array<any>).map(
    entry => new ModelEntity(
      entry.name,
      entry.version,
      entry.domain,
      entry.modelFramework,
      entry.modelLocation,
      entry.outputs,
      entry.id
    )
  );
  return models;
}