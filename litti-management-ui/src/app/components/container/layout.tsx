import { Col, Row } from "reactstrap"
import SidebarProducts, { NAV_OPTION_MODELS } from "../sidebar/products/page"
import "@/app/styles/entity.css"
import "@/app/styles/sidebar.css"
import { useState } from "react"

export default function RootContainerLayout({
    children, // will be a page or nested layout
  }: {
    children: React.ReactNode
  }) {
    let [navOption, setNavOption] = useState(NAV_OPTION_MODELS);
    return (
      <section>
        {/* Include shared UI here e.g. a header or sidebar */}
        <nav></nav>
        <Row>
          <Col className="sidebar"><SidebarProducts navOption={navOption} setNavOption={setNavOption}/></Col>
          <Col xs={10} className="entity-base">
            {children}
          </Col>
        </Row>
      </section>
    )
  }