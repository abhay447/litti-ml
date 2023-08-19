import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity"

export  async function getFeatureGroups(){
    const res = await fetch('http://localhost:8081/feature-groups');
  const data = await res.json();
  const features = (data as Array<any>).map(
    entry => new FeatureGroupEntity(
      entry.name,
      entry.dimensions,
      entry.id
    )
  );
  return features;
}