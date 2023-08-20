"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {ModelEntity} from '@/app/entities/modelEntity';
import "@/app/styles/entity.css"
import { OPTION_LIST } from "@/app/components/common/menu/page";
import { addModel } from "@/app/services/modelService";

const handleSubmit = async (event:any, setSelectedOption: any) => {
  // Stop the form from submitting and refreshing the page.
  event.preventDefault()

  // Get data from the form.
  const modelEntity = new ModelEntity(
    (event.target.modelName as HTMLInputElement).value,
    (event.target.modelVersion as HTMLInputElement).value,
    (event.target.modelDomain as HTMLInputElement).value,
    (event.target.modelFramework as HTMLInputElement).value,
    (event.target.modelLocation as HTMLInputElement).value,
    (event.target.modelOutputs as HTMLInputElement).value
  );

  await addModel(modelEntity)
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
                <Col xs={1}><label htmlFor="modelLocation">Location</label></Col>
                <Col xs={8}><input type="text" className="entity-text-input" id="modelLocation" name="modelLocation" /></Col>
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