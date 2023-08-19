import { FeatureEntity } from "@/app/entities/featureEntity"

export  async function getFeatures(){
    const res = await fetch('http://localhost:8081/features');
  const data = await res.json();
  const features = (data as Array<any>).map(
    entry => new FeatureEntity(
      entry.name,
      entry.version,
      entry.dataType,
      entry.defaultValue,
      entry.featureGroupId,
      entry.ttlSeconds,
      entry.id
    )
  );
  return features;
}