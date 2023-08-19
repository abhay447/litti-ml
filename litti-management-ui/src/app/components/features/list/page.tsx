"use client";
import { FeatureEntity } from "@/app/entities/featureEntity";
import { useEffect, useState } from "react"
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import { getFeatureGroups } from "@/app/services/featureGroupService";
import { getFeatures } from "@/app/services/featureService";
import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity";


// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function FeatureListComponent() {

  const [data, setData] = useState<FeatureEntity[]>()
  const [isLoading, setLoading] = useState(true)
  const [featureGroups, setFeatureGroups] = useState<FeatureGroupEntity[]>()
  useEffect(() => {
    Promise.all([
      getFeatures(),
      getFeatureGroups()
    ]).then(([features, featureGroups]) => {
      setData(features);
      setFeatureGroups(featureGroups);
      setLoading(false);
    });
  }, [])
 
  if (isLoading) return <p>Loading...</p>
  if (!data) return <p>No profile data</p>

  const features = (data as Array<any>).map(
    entry => new FeatureEntity(
      entry.name,
      entry.version,
      entry.dataType,
      entry.defaultValue,
      entry.featureGroupId,
      entry.ttlSeconds,
      entry.id
    )
  )
 
  return (
      <Row xs={10}>
        <Row key="HEADER">
          <Col>NAME</Col>
          <Col>VERSION</Col>
          <Col>DATA TYPE</Col>
          <Col>DEFAULT VALUE</Col>
          <Col>FEATURE GROUP</Col>
          <Col>TTL SECONDS</Col>
        </Row>
        {
          features.map(feature =>{
            console.log(feature);
              return <Row key={feature.id}>
                <Col>{feature.name}</Col>
                <Col>{feature.version}</Col>
                <Col>{feature.dataType}</Col>
                <Col>{feature.defaultValue}</Col>
                <Col>{feature.featureGroupId}</Col>
                <Col>{feature.ttlSeconds}</Col>
              </Row>
            }
          )
        }
      </Row>
  )

}