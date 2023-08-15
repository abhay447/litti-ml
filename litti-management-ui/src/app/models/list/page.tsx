"use client";
import { ModelEntity } from "@/app/entities/modelEntity";
import { useEffect, useState } from "react"
import { Col, Container, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../globals.css'

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function Page() {

  const [data, setData] = useState(null)
  const [isLoading, setLoading] = useState(true)
 
  useEffect(() => {
    fetch('http://localhost:8081/models')
      .then((res) => res.json())
      .then((data) => {
        setData(data)
        setLoading(false)
      })
  }, [])
 
  if (isLoading) return <p>Loading...</p>
  if (!data) return <p>No profile data</p>

  const models = (data as Array<any>).map(
    entry => new ModelEntity(
      entry.name,
      entry.version,
      entry.domain,
      entry.modelFramework,
      entry.outputs,
      entry.id
    )
  )
 
  return (
    <Container>
      {
        models.map(model =>{
          console.log(model);
            return <Row key={model.id}>
              <Col>{model.name}</Col>
              <Col>{model.version}</Col>
              <Col>{model.modelFramework}</Col>
              <Col>{model.domain}</Col>
              <Col>{model.outputs}</Col>
            </Row>
        }
      )
      }
    </Container>
  )

}