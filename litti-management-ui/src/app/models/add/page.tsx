"use client";
import { Col, Container, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../globals.css'
import {ModelEntity} from '../../entities/modelEntity';


const handleSubmit = async (event:any) => {
  // Stop the form from submitting and refreshing the page.
  event.preventDefault()

  // Get data from the form.
  const modelEntity = new ModelEntity(
    (event.target.modelName as HTMLInputElement).value,
    (event.target.modelVersion as HTMLInputElement).value,
    (event.target.modelDomain as HTMLInputElement).value,
    (event.target.modelFramework as HTMLInputElement).value,
    (event.target.modelOutputs as HTMLInputElement).value
  );

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
  const result = await response.json()
  alert(`Is this your full name: ${result.data}`)
}

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function Page() {
    return <form onSubmit={handleSubmit}>
      <Row>
        <Col><label htmlFor="modelName">Name</label></Col>
        <Col xs={10}><input type="text" id="modelName" name="modelName" /></Col>
      </Row>
      <Row>
        <Col><label htmlFor="modelVersion">Version</label></Col>
        <Col xs={10}><input type="text" id="modelVersion" name="modelVersion" /></Col>
      </Row>
      <Row>
        <Col><label htmlFor="modelDomain">Domain</label></Col>
        <Col xs={10}><input type="text" id="modelDomain" name="modelDomain" /></Col>
      </Row>
      <Row>
        <Col><label htmlFor="modelFramework">Framework</label></Col>
        <Col xs={10}><input type="text" id="modelFramework" name="modelFramework" /></Col>
      </Row>
      <Row>
        <Col><label htmlFor="modelOutputs">Outputs</label></Col>
        <Col xs={10}><input type="text" id="modelOutputs" name="modelOutputs" /></Col>
      </Row>
      <Row>
        <Col xs={12}><button type="submit">Submit</button></Col>
      </Row>
  </form>
  }