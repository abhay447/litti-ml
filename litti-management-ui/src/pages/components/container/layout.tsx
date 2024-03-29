import { Col, Row } from "reactstrap"
import SidebarProducts from "../sidebar/products/page"
import "@/app/styles/entity.css"
import "@/app/styles/sidebar.css"
import { useState } from "react"

export default function RootContainerComponent({
    children, // will be a page or nested layout
    navOption,
    setNavOption
  }: {
    children: React.ReactNode
    navOption: string,
    setNavOption: any,
  }) {
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