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

export async function addModel(modelEntity:ModelEntity){
  const data = {
    modelEntity: modelEntity,
    featureIds: []
  }

  // Send the data to the server in JSON format.
  const JSONdata = JSON.stringify(data)

  // API endpoint where we send form data.
  const endpoint = 'http://localhost:8081/models'

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