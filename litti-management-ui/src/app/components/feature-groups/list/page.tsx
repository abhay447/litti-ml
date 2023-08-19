"use client";
import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity";
import { useEffect, useState } from "react"
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {getFeatureGroups} from '@/app/services/featureGroupService';


// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function FeatureGroupsListComponent() {

  const [featureGroups, setFeatureGroups] = useState<Array<FeatureGroupEntity>>([])
  const [isLoading, setLoading] = useState(true)
 
  useEffect(() => {

    getFeatureGroups()
      .then((data) => {
        setFeatureGroups(data)
        setLoading(false)
      })
  }, []);
 
  if (isLoading) return <p>Loading...</p>
  if (featureGroups.length == 0) return <p>No profile data</p>

 
  return (
      <Row xs={10}>
        <Row key="HEADER">
          <Col>NAME</Col>
          <Col>DIMENSIONS</Col>
        </Row>
        {
          featureGroups.map(featureGroup =>{
            console.log(featureGroup);
              return <Row key={featureGroup.id}>
                <Col>{featureGroup.name}</Col>
                <Col>{featureGroup.dimensions}</Col>
              </Row>
            }
          )
        }
      </Row>
  )

}