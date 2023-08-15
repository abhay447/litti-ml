"use client";
import { Col, Container, Row } from "reactstrap";


// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function Page() {
    return <form action="/send-data-here" method="post">
    <Container>
    <Row>
      {/* <div className="col-sm-4">
        <label htmlFor="modelName">Name</label>
      </div>
      <div className="col-sm-4">
        <input type="text" id="modelName" name="modelName" />
      </div> */}
      <Col>.col-sm-4</Col>
      <Col>.col-sm-4</Col>
      <Col>.col-sm-4</Col>
    </Row>
    </Container>

    <div className="row">
  <div className="col-sm-4">.col-sm-4</div>
  <div className="col-sm-4">.col-sm-4</div>
  <div className="col-sm-4">.col-sm-4</div>
</div>

    <div>
      <label htmlFor="modelVersion">Version</label>
      <input type="text" id="modelVersion" name="modelVersion" />
    </div>

    <div>
      <label htmlFor="modelDomain">Domain</label>
      <input type="text" id="modelDomain" name="modelDomain" />
    </div>

    <div>
      <label htmlFor="modelFramework">Framework</label>
      <input type="text" id="modelFramework" name="modelFramework" />
    </div>

    <div>
      <label htmlFor="modelOutputs">Outputs</label>
      <input type="text" id="modelOutputs" name="modelOutputs" />
    </div>

    <button type="submit">Submit</button>
  </form>
  }