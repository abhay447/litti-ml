import { ModelEntity } from "@/app/entities/modelEntity"
import { getServerSideProps } from "@/app/common/clients_config";

export  async function getModels(){
  const endpoint = (await getServerSideProps()).props.base_url+'/models'
    const res = await fetch(endpoint);
  const data = await res.json();
  const models = (data as Array<any>).map(
    entry => new ModelEntity(
      entry.name,
      entry.version,
      entry.domain,
      entry.modelFramework,
      entry.modelLocation,
      JSON.stringify(entry.outputs),
      entry.id
    )
  );
  return models;
}

export async function addModel(modelEntity:ModelEntity, featureIds:string[]){
  const data = {
    modelEntity: modelEntity,
    featureIds: featureIds
  }

  // Send the data to the server in JSON format.
  const jsonData = JSON.stringify(data)

  // API endpoint where we send form data.
  const endpoint = (await getServerSideProps()).props.base_url+'/models'

  // Form the request for sending data to the server.
  const options = {
    // The method is POST because we are sending data.
    method: 'POST',
    // Tell the server we're sending JSON.
    headers: {
      'Content-Type': 'application/json',
    },
    // Body of the request is the JSON data we created above.
    body: jsonData,
  }

  // Send the form data to our forms API on Vercel and get a response.
  const response = await fetch(endpoint, options)
  const responseJson = await response.json()

  // Get the response data from server as JSON.
  // If server returns the name submitted, that means the form works.
  const result = JSON.stringify(responseJson)
  console.log(result);
  return responseJson.id as string
}