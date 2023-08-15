"use client";
import { ModelEntity } from "@/app/entities/modelEntity";
import { useEffect, useState } from "react"
import { Col, Container, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../globals.css'

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function SidebarProducts() { 
  return (
    
    <Container>
      <Row>
        Models
      </Row>
      <Row>
        Features
      </Row>
      <Row>
        Model Clusters
      </Row>
      <Row>
        Feature Stores
      </Row>
    </Container>
  )

}