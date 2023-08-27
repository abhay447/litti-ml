import { FeatureEntity } from "@/app/entities/featureEntity"
import { NODE_SERVER_PROXY_PREFIX } from "@/app/common/clients_config";

export  async function getFeatures(){
  const endpoint = NODE_SERVER_PROXY_PREFIX+'/features'
  const res = await fetch(endpoint);
  const data = await res.json();
  const features:FeatureEntity[] = (data as Array<any>).map(
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

export async function addFeature(featureEntity:FeatureEntity) {
    // Send the data to the server in JSON format.
    const JSONdata = JSON.stringify(featureEntity)
    console.log(JSONdata)
  
    // API endpoint where we send form data.
    const endpoint = NODE_SERVER_PROXY_PREFIX+'/features'
  
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
    const jsonResponse = await response.json()
    const result = JSON.stringify(jsonResponse)
    console.log(result);
    return jsonResponse.id as string;
}