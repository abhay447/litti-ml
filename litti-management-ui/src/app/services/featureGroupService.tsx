import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity"
import { NODE_SERVER_PROXY_PREFIX } from "@/app/common/clients_config";

export  async function getFeatureGroups():Promise<FeatureGroupEntity[]>{
  const endpoint = NODE_SERVER_PROXY_PREFIX+'/feature-groups'
    const res = await fetch(endpoint);
  const data = await res.json();
  const features = (data as Array<any>).map(
    entry => new FeatureGroupEntity(
      entry.name,
      JSON.stringify(entry.dimensions),
      entry.id
    )
  );
  return features;
}

export  async function getFeatureGroupsMap():Promise<Map<string,FeatureGroupEntity>>{
  const featureGroups = await getFeatureGroups();
  return new Map(featureGroups.map(fg => [fg.id,fg]));
}

export  async function getFeatureGroupsNameMap():Promise<Map<string,FeatureGroupEntity>>{
  const featureGroups = await getFeatureGroups();
  return new Map(featureGroups.map(fg => [fg.name,fg]));
}

export async function addFeautureGroup(featureGroupEntity:FeatureGroupEntity) {
   // Send the data to the server in JSON format.
   const JSONdata = JSON.stringify(featureGroupEntity)
   console.log(JSONdata)
 
   // API endpoint where we send form data.
   const endpoint = NODE_SERVER_PROXY_PREFIX+'/feature-groups'
 
   // Form the request for sending data to the server.
   const options = {
     // The method is POST because we are sending data.
     method: 'POST',
     // Tell the server we're sending JSON.
     headers: {
       'Content-Type': 'application/json',
     },
     // Body of the request is the JSON data we created above.
     body: JSONdata,
   }
 
   // Send the form data to our forms API on Vercel and get a response.
   const response = await fetch(endpoint, options)
 
   // Get the response data from server as JSON.
   // If server returns the name submitted, that means the form works.
   const result = JSON.stringify(await response.json())
   console.log(result);
  
}