"use client";
import { Button, ButtonToggle, Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {ModelEntity} from '@/app/entities/modelEntity';
import "@/app/styles/entity.css"
import { addModel, uploadArtifact } from "@/app/services/modelService";
import { useState } from "react";
import { addFeature, getFeatures } from "@/app/services/featureService";
import { FeatureEntity } from "@/app/entities/featureEntity";
import LoaderComponent from "@/pages/components/common/loader/loading";
import Multiselect from 'multiselect-react-dropdown';
import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity";
import { getFeatureGroupsNameMap } from "@/app/services/featureGroupService";
import { OPTION_LIST } from "@/app/common/constants";
import { useFilePicker } from "use-file-picker";
import { ArtifactEntity } from "@/app/entities/artifactEntity";

async function uploadModel(modelEntity:ModelEntity, plainFiles:File[], featureIds:string[],setSelectedOption: any){
  const modelArtifactId = await uploadArtifact(plainFiles[0])
  modelEntity.modelArtifactId = modelArtifactId
  console.log(modelEntity)
  await addModel(modelEntity, featureIds)
  setSelectedOption(OPTION_LIST);
}

const handleSubmit = async (event:any, setSelectedOption: any, selectedFeatureIds: string[], plainFiles: File[]) => {
  // Stop the form from submitting and refreshing the page.
  event.preventDefault()
  // Get data from the form.
  const modelEntity = new ModelEntity(
    (event.target.modelName as HTMLInputElement).value,
    (event.target.modelVersion as HTMLInputElement).value,
    (event.target.modelDomain as HTMLInputElement).value,
    (event.target.modelFramework as HTMLInputElement).value,
    (event.target.modelOutputs as HTMLInputElement).value,
    undefined
  );
  await uploadModel(modelEntity,plainFiles,selectedFeatureIds,setSelectedOption)
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
    return <ModelAddJsonForm features={features} featureGroupsMap={featureGroupsMap} setSelectedOption={setSelectedOption} />
  }
}

function ModelAddForm(props:any) {

  const [openFileSelector, { plainFiles, loading }] = useFilePicker({
    multiple: false,
  });

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
  <form onSubmit={(e) => handleSubmit(e,props.setSelectedOption, props.selectedFeatureIds, plainFiles)}>
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
      <Col xs={2}><label htmlFor="modelLocation">Location</label></Col>
      <Col xs={2}><label id="modelLocation" />{plainFiles?.at(0)?.name}</Col>
      <Col xs={2}><Button id="fileUploadButton" onClick={() => openFileSelector()}>Select Model File</Button></Col>
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
  const [openFileSelector, { plainFiles, loading }] = useFilePicker({
    multiple: false,
  });
  return <form onSubmit={(e) => handleJsonSubmit(e,props.features, props.featureGroupsMap,plainFiles,props.setSelectedOption)}>
    <Row className="entity-base-row">
      <label htmlFor="addModelJSON">Add model json</label>
      <textarea className="entity-text-input" id="addModelJSON" name="addModelJSON" rows={20}/>
    </Row>
    <Row className="entity-base-row">
      <Col xs={2}><label htmlFor="modelLocation">Location</label></Col>
      <Col xs={2}><label id="modelLocation" />{plainFiles?.at(0)?.name}</Col>
      <Col xs={2}><Button id="fileUploadButton" onClick={() => openFileSelector()}>Select Model File</Button></Col>
    </Row>
    <Row className="entity-base-row">
      <Col xs={9}><button type="submit" className="entity-submit-button">Submit</button></Col>
    </Row> 
  </form>

}

const handleJsonSubmit = async (event:any, allFeatures:FeatureEntity[], featureGroupsMap: Map<String,FeatureGroupEntity>, plainFiles:File[],setSelectedOption:any) => {
  event.preventDefault();
  const jsonString = (event.target.addModelJSON as HTMLInputElement).value
  const data = JSON.parse(jsonString);
  const modelEntity = new ModelEntity(
    data.name,
    data.version,
    data?.domain,
    data.modelFramework,
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
    return uploadModel(modelEntity,plainFiles,featureIds,setSelectedOption)
  }).then((modelId) => {
    console.log(modelId);
  })
}