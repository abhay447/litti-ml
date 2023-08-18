"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {FeatureEntity} from '@/app/entities/featureEntity';
import "@/app/styles/entity.css"
import { OPTION_LIST } from "@/app/components/common/menu/page";

const handleSubmit = async (event:any, setSelectedOption: any) => {
  // Stop the form from submitting and refreshing the page.
  event.preventDefault()

  // Get data from the form.
  const modelEntity = new FeatureEntity(
    (event.target.featureName as HTMLInputElement).value,
    (event.target.featureVersion as HTMLInputElement).value,
    (event.target.featureDataType as HTMLInputElement).value,
    (event.target.featureDefaultValue as HTMLInputElement).value,
    (event.target.featureGroupId as HTMLInputElement).value,
    Number.parseInt((event.target.ttlSeconds as HTMLInputElement).value)
  );

  const data = {
    modelEntity: modelEntity,
    featureIds: []
  }

  // Send the data to the server in JSON format.
  const JSONdata = JSON.stringify(data)

  // API endpoint where we send form data.
  const endpoint = 'http://localhost:8081/features'

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
export default function FeatureAddComponent(props:any) {
    const setSelectedOption = props.setSelectedOption;
    return <Row>
            <form onSubmit={(e) => handleSubmit(e,setSelectedOption)}>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="featureName">Name</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="featureName" name="featureName" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="featureVersion">Version</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="featureVersion" name="featureVersion" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="featureDataType">Domain</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="featureDataType" name="featureDataType" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="featureDefaultValue">Framework</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="featureDefaultValue" name="featureDefaultValue" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="featureGroupId">Outputs</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="featureGroupId" name="featureGroupId" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="ttlSeconds">Outputs</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="ttlSeconds" name="ttlSeconds" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={9}><button type="submit" className="entity-submit-button">Submit</button></Col>
              </Row>          
            </form>
          </Row>
  }