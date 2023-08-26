"use client";
import { ButtonToggle, Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {ModelEntity} from '@/app/entities/modelEntity';
import "@/app/styles/entity.css"
import { addModel } from "@/app/services/modelService";
import { useState } from "react";
import { addFeature, getFeatures } from "@/app/services/featureService";
import { FeatureEntity } from "@/app/entities/featureEntity";
import LoaderComponent from "@/pages/components/common/loader/loading";
import Multiselect from 'multiselect-react-dropdown';
import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity";
import { getFeatureGroupsNameMap } from "@/app/services/featureGroupService";
import { OPTION_LIST } from "@/app/common/constants";

const handleSubmit = async (event:any, setSelectedOption: any, selectedFeatureIds: string[]) => {
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

  await addModel(modelEntity, selectedFeatureIds)
  setSelectedOption(OPTION_LIST);
}

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function ModelAddComponent(props:any) {
    const setSelectedOption = props.setSelectedOption;

    const [features, setFeatures] = useState<FeatureEntity[]>([])
    const [featureGroupsMap, setFeatureGroupsMap] = useState<Map<string, FeatureGroupEntity>>(new Map())
    const [selectedFeatureIds, setSelectedFeatures] = useState<FeatureEntity[]>([])
    const [isLoading, setIsLoading] = useState<boolean>(true)
    const [useFormInput, setUseFormInput] = useState<boolean>(true)
    Promise.all([getFeatures(),getFeatureGroupsNameMap()])
      .then(([featureData,featureGroupsMap]) => {
      setFeatures(featureData);
      setFeatureGroupsMap(featureGroupsMap);
      setIsLoading(false);
    });

    return <Row>
      <ButtonToggle onClick={(e) => {setUseFormInput(!useFormInput)}}>{useFormInput? "Use Json input" : "Use Form Input"}</ButtonToggle>
      {chooseRenderComponent(useFormInput,isLoading,setSelectedOption,features, featureGroupsMap,setSelectedFeatures,selectedFeatureIds)}
    </Row>

  }

function chooseRenderComponent(useFormInput:boolean, isLoading: boolean, setSelectedOption:any, features: FeatureEntity[], featureGroupsMap: Map<string, FeatureGroupEntity>, setSelectedFeatureIds:any, selectedFeatureIds:FeatureEntity[]) {
  if(isLoading){
    return <LoaderComponent/>
  }
  if(useFormInput){
      return <ModelAddForm setSelectedOption={setSelectedOption} features={features} setSelectedFeatureIds={setSelectedFeatureIds} selectedFeatureIds={selectedFeatureIds}/>
  } else {
    return <ModelAddJsonForm features={features} featureGroupsMap={featureGroupsMap}/>
  }
}

function ModelAddForm(props:any) {

  function onSelectFeature(selectedList:any[], selectedItem:any) {
    selectedList.concat(selectedItem);
    updateSelectedFeatureIds(selectedList)
  }
  
  function onRemoveFeature(selectedList:any[], selectedItem:any) {
    selectedList = selectedList.filter(obj => obj !== selectedItem)
    updateSelectedFeatureIds(selectedList)
  }

  function updateSelectedFeatureIds(selectedList:any[]){
    props.setSelectedFeatureIds(selectedList.map((f) => {return f.value}));
    console.log(props.selectedFeatureIds)
  }
  
  return <Row>
  <form onSubmit={(e) => handleSubmit(e,props.setSelectedOption, props.selectedFeatureIds)}>
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
      <Col xs={1}><label htmlFor="features">Features</label></Col>
      <Col xs={8}>
        <Multiselect
          options={getFeaturePickerOptions(props.features)}
          selectedValues={[]}
          displayValue="label"
          onSelect={onSelectFeature}
          onRemove={onRemoveFeature}
         />
      </Col>
    </Row>
    <Row className="entity-base-row">
      <Col xs={9}><button type="submit" className="entity-submit-button">Submit</button></Col>
    </Row>          
  </form>
</Row>
}

function getFeaturePickerOptions(features:FeatureEntity[]){
  return features.map((feature) => {
    return {value:feature.id, label:feature.name+"|"+feature.version+"|"+feature.dataType+"|"+feature.defaultValue};
  });
}

function ModelAddJsonForm(props:any) {
  return <form onSubmit={(e) => handleJsonSubmit(e,props.features, props.featureGroupsMap)}>
    <Row className="entity-base-row">
      <label htmlFor="addModelJSON">Add model json</label>
      <textarea className="entity-text-input" id="addModelJSON" name="addModelJSON" rows={20}/>
    </Row>
    <Row className="entity-base-row">
      <Col xs={9}><button type="submit" className="entity-submit-button">Submit</button></Col>
    </Row> 
  </form>

}

const handleJsonSubmit = async (event:any, allFeatures:FeatureEntity[], featureGroupsMap: Map<String,FeatureGroupEntity>) => {
  event.preventDefault();
  const jsonString = (event.target.addModelJSON as HTMLInputElement).value
  const data = JSON.parse(jsonString);
  const modelEntity = new ModelEntity(
    data.name,
    data.version,
    data?.domain,
    data.modelFramework,
    data.modelLocation,
    JSON.stringify(data.outputs)
  );
  const rawFeatures = data.features;
  const allFeaturesMap = new Map(allFeatures.map((f) => [f.name+"|"+f.version,f]))
  Promise.all(rawFeatures.map(
    async (rawFeature:any) => {
      const existingFeature = allFeaturesMap.get(rawFeature.name+"|"+rawFeature.version);
      const featureGroup = featureGroupsMap.get(rawFeature.featureGroup)
      let errorMsg = ""
      if(featureGroup == null) {
        errorMsg = "feature group not found: " + rawFeature.featureGroup
        alert(errorMsg)
        throw new Error(errorMsg)
      }
      if(existingFeature == null) {
        const newFeatureEntity = new FeatureEntity(
          rawFeature.name,rawFeature.version,rawFeature.dataType,rawFeature.defaultValue,
          featureGroup!.id,rawFeature.ttlSeconds
        )
        return await addFeature(newFeatureEntity)
      } else if(!existingFeature.equalsRawFeature(rawFeature)){
        errorMsg = "features not equal: " + JSON.stringify(existingFeature) + " , " + JSON.stringify(rawFeature);
        alert(errorMsg)
        throw new Error(errorMsg)
      }
      return new Promise((resolve) => resolve(existingFeature?.id));
    }
  )).then((featureIds) => {
    console.log(modelEntity)
    console.log(rawFeatures.length)
    console.log(featureIds)
    return addModel(modelEntity,featureIds)
  }).then((modelId) => {
    console.log(modelId);
  })
}