"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {FeatureEntity} from '@/app/entities/featureEntity';
import "@/app/styles/entity.css"
import { OPTION_LIST } from "@/app/components/common/menu/page";
import { useState } from "react";
import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity";
import { getFeatureGroups } from "@/app/services/featureGroupService";
import LoaderComponent from "../../common/loader/loading";
import Select from 'react-select';

const handleSubmit = async (event:any, setSelectedOption: any, selectedFeatureGroup:any) => {
  // Stop the form from submitting and refreshing the page.
  event.preventDefault()
  console.log(selectedFeatureGroup)

  // Get data from the form.
  const featureEntity = new FeatureEntity(
    (event.target.featureName as HTMLInputElement).value,
    (event.target.featureVersion as HTMLInputElement).value,
    (event.target.featureDataType as HTMLInputElement).value,
    (event.target.featureDefaultValue as HTMLInputElement).value,
    selectedFeatureGroup.value,
    Number.parseInt((event.target.ttlSeconds as HTMLInputElement).value)
  );

  // Send the data to the server in JSON format.
  const JSONdata = JSON.stringify(featureEntity)
  console.log(JSONdata)

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
const featureGroupsPromise = getFeatureGroups();

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function FeatureAddComponent(props:any) {

    const [featureGroups, setFeatureGroups] = useState<Array<FeatureGroupEntity>>([])
    const [selectedFeatureGroup, selectFeatureGroup] = useState()
    const [isLoading, setIsLoading] = useState<boolean>(true)
    const setSelectedOption = props.setSelectedOption;
    featureGroupsPromise.then((data)=> {
      setFeatureGroups(data);
      setIsLoading(false);
    });
    return chooseRenderComponent(isLoading, setSelectedOption,featureGroups,selectFeatureGroup, selectedFeatureGroup);
}

export function chooseRenderComponent(isLoading: boolean, setSelectedOption:any, featureGroups: FeatureGroupEntity[], selectFeatureGroup:any, selectedFeatureGroup:any) {
  if(isLoading){
    return <LoaderComponent/>
  } else {
    console.log(isLoading)
    console.log(featureGroups)
    return <FeatureAddForm setSelectedOption={setSelectedOption} featureGroups={featureGroups} selectFeatureGroup={selectFeatureGroup} selectedFeatureGroup={selectedFeatureGroup}/>
  }
}

export function FeatureAddForm(props:any ) {
  const featureGroups:FeatureGroupEntity[] = props.featureGroups;
  const featureGroupSelectOptions = featureGroups.map(
    x => {return {value:x.id, label:x.name}}
  )
  return <Row>
  <form onSubmit={(e) => handleSubmit(e,props.setSelectedOption, props.selectedFeatureGroup)}>
    <Row className="entity-base-row">
      <Col xs={2}><label htmlFor="featureName">Name</label></Col>
      <Col xs={8}><input type="text" className="entity-text-input" id="featureName" name="featureName" /></Col>
    </Row>
    <Row className="entity-base-row">
      <Col xs={2}><label htmlFor="featureVersion">Version</label></Col>
      <Col xs={8}><input type="text" className="entity-text-input" id="featureVersion" name="featureVersion" /></Col>
    </Row>
    <Row className="entity-base-row">
      <Col xs={2}><label htmlFor="featureDataType">Data Type</label></Col>
      <Col xs={8}><input type="text" className="entity-text-input" id="featureDataType" name="featureDataType" /></Col>
    </Row>
    <Row className="entity-base-row">
      <Col xs={2}><label htmlFor="featureDefaultValue">Default Value</label></Col>
      <Col xs={8}><input type="text" className="entity-text-input" id="featureDefaultValue" name="featureDefaultValue" /></Col>
    </Row>
    <Row className="entity-base-row">
      <Col xs={2}><label htmlFor="featureGroupId">Feature Group</label></Col>
      <Col xs={8}><Select
          defaultValue={featureGroupSelectOptions[0]}
          onChange={props.selectFeatureGroup}
          options={featureGroupSelectOptions}
        />
      </Col>
    </Row>
    <Row className="entity-base-row">
      <Col xs={2}><label htmlFor="ttlSeconds">TTL</label></Col>
      <Col xs={8}><input type="text" className="entity-text-input" id="ttlSeconds" name="ttlSeconds" /></Col>
    </Row>
    <Row className="entity-base-row">
      <Col xs={10}><button type="submit" className="entity-submit-button">Submit</button></Col>
    </Row>          
  </form>
</Row>
}