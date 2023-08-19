"use client";
import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity";
import { useEffect, useState } from "react"
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'


// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function FeatureGroupsListComponent() {

  const [data, setData] = useState(null)
  const [isLoading, setLoading] = useState(true)
 
  useEffect(() => {
    fetch('http://localhost:8081/feature-groups')
      .then((res) => res.json())
      .then((data) => {
        setData(data)
        setLoading(false)
      })
  }, [])
 
  if (isLoading) return <p>Loading...</p>
  if (!data) return <p>No profile data</p>

  const features = (data as Array<any>).map(
    entry => new FeatureGroupEntity(
      entry.name,
      entry.dimensions
    )
  )
 
  return (
      <Row xs={10}>
        <Row key="HEADER">
          <Col>NAME</Col>
          <Col>DIMENSIONS</Col>
        </Row>
        {
          features.map(feature =>{
            console.log(feature);
              return <Row key={feature.id}>
                <Col>{feature.name}</Col>
                <Col>{feature.dimensions}</Col>
              </Row>
            }
          )
        }
      </Row>
  )

}