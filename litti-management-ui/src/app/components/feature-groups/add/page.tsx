"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {FeatureGroupEntity} from '@/app/entities/featureGroupEntity';
import "@/app/styles/entity.css"
import { OPTION_LIST } from "@/app/components/common/menu/page";
import { addFeautureGroup } from "@/app/services/featureGroupService";

const handleSubmit = async (event:any, setSelectedOption: any) => {
  // Stop the form from submitting and refreshing the page.
  event.preventDefault()

  // Get data from the form.
  const featureGroupEntity = new FeatureGroupEntity(
    (event.target.featureGroupName as HTMLInputElement).value,
    (event.target.featureGroupDimensions as HTMLInputElement).value
  );

  await addFeautureGroup(featureGroupEntity)
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