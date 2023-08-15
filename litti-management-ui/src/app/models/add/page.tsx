"use client";
import { Col, Container, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../globals.css'


// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function Page() {
    return <form action="/send-data-here" method="post">
      <Row>
        <Col><label htmlFor="modelName">Name</label></Col>
        <Col xs={10}><input type="text" id="modelName" name="modelName" /></Col>
      </Row>
      <Row>
        <Col><label htmlFor="modelVersion">Version</label></Col>
        <Col xs={10}><input type="text" id="modelVersion" name="modelVersion" /></Col>
      </Row>
      <Row>
        <Col><label htmlFor="modelDomain">Domain</label></Col>
        <Col xs={10}><input type="text" id="modelDomain" name="modelDomain" /></Col>
      </Row>
      <Row>
        <Col><label htmlFor="modelFramework">Framework</label></Col>
        <Col xs={10}><input type="text" id="modelFramework" name="modelFramework" /></Col>
      </Row>
      <Row>
        <Col><label htmlFor="modelOutputs">Outputs</label></Col>
        <Col xs={10}><input type="text" id="modelOutputs" name="modelOutputs" /></Col>
      </Row>
      <Row>
        <Col xs={12}><button type="submit">Submit</button></Col>
      </Row>
  </form>
  }