import { Col, Row } from "reactstrap"
import SidebarProducts from "@/app/components/sidebar/products/page"
import "@/app/styles/entity.css"
import "@/app/styles/sidebar.css"
import RootContainerLayout from "@/app/components/container/layout"
import ModelMenu from "@/app/components/models/menu/page"

export default function ModelContainerLayout({
    children, // will be a page or nested layout
  }: {
    children: React.ReactNode
  }) {
    return (
      <section>
        {/* Include shared UI here e.g. a header or sidebar */}
        <nav></nav>
        <RootContainerLayout>
            <Row>
              <ModelMenu/>
            </Row>
            {children}
        </RootContainerLayout>
      </section>
    )
  }