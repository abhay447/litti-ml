import { Col, Row } from "reactstrap"
import SidebarProducts from "../sidebar/products/page"

export default function RootContainerLayout({
    children, // will be a page or nested layout
  }: {
    children: React.ReactNode
  }) {
    return (
      <section>
        {/* Include shared UI here e.g. a header or sidebar */}
        <nav></nav>
        <Row>
          <Col xs={2}><SidebarProducts/></Col>
          <Col xs={10}>
            {children}
          </Col>
        </Row>
      </section>
    )
  }