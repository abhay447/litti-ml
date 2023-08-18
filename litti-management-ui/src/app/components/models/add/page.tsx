"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {ModelEntity} from '@/app/components/entities/modelEntity';
import "@/app/styles/entity.css"
import { OPTION_LIST } from "@/app/components/common/menu/page";

const handleSubmit = async (event:any, setSelectedOption: any) => {
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
  const result = JSON.stringify(await response.json())
  console.log(result);
  setSelectedOption(OPTION_LIST);
}

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function ModelAddComponent(props:any) {
    const setSelectedOption = props.setSelectedOption;
    return <Row>
            <form onSubmit={(e) => handleSubmit(e,setSelectedOption)}>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="modelName">Name</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="modelName" name="modelName" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="modelVersion">Version</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="modelVersion" name="modelVersion" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="modelDomain">Domain</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="modelDomain" name="modelDomain" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="modelFramework">Framework</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="modelFramework" name="modelFramework" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="modelOutputs">Outputs</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="modelOutputs" name="modelOutputs" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={9}><button type="submit" className="entity-submit-button">Submit</button></Col>
              </Row>          
            </form>
          </Row>
  }