"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {FeatureGroupEntity} from '@/app/entities/featureGroupEntity';
import "@/app/styles/entity.css"
import { OPTION_LIST } from "@/app/components/common/menu/page";

const handleSubmit = async (event:any, setSelectedOption: any) => {
  // Stop the form from submitting and refreshing the page.
  event.preventDefault()

  // Get data from the form.
  const featureEntity = new FeatureGroupEntity(
    (event.target.featureGroupName as HTMLInputElement).value,
    (event.target.featureGroupDimensions as HTMLInputElement).value
  );

  // Send the data to the server in JSON format.
  const JSONdata = JSON.stringify(featureEntity)
  console.log(JSONdata)

  // API endpoint where we send form data.
  const endpoint = 'http://localhost:8081/feature-groups'

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
export default function FeatureGroupAddComponent(props:any) {
    const setSelectedOption = props.setSelectedOption;
    return <Row>
            <form onSubmit={(e) => handleSubmit(e,setSelectedOption)}>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="featureGroupName">Name</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="featureGroupName" name="featureGroupName" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={1}><label htmlFor="featureGroupDimensions">Dimensions</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="featureGroupDimensions" name="featureGroupDimensions" /></Col>
              </Row>
              <Row className="entity-base-row">
                <Col xs={9}><button type="submit" className="entity-submit-button">Submit</button></Col>
              </Row>          
            </form>
          </Row>
  }