import { Col, Row } from "reactstrap"
import SidebarProducts from "../sidebar/products/page"
import "@/app/styles/entity.css"
import "@/app/styles/sidebar.css"

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
          <Col className="sidebar"><SidebarProducts/></Col>
          <Col xs={10} className="entity-base">
            {children}
          </Col>
        </Row>
      </section>
    )
  }