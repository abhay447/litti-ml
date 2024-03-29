"use client";
import { Container, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/styles/sidebar.css'
import { NAV_OPTION_FEATURES, NAV_OPTION_FEATURE_GROUPS, NAV_OPTION_FEATURE_STORES, NAV_OPTION_MODELS, NAV_OPTION_MODEL_CLUSTERS } from "@/app/common/constants";

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL

const sidebarProductOptions = [
  NAV_OPTION_MODELS,NAV_OPTION_FEATURES,NAV_OPTION_FEATURE_GROUPS,NAV_OPTION_MODEL_CLUSTERS,NAV_OPTION_FEATURE_STORES
];

export default function SidebarProducts(props: any) {
  const navOption = props.navOption;
  const setNavOption = props.setNavOption;

  function getOptionStyle(option: string) {
    if (option === navOption) {
      return "sidebar-item sidebar-item-selected";
    }
    return "sidebar-item";
  }

  function handleOnClick(option: string) {
    setNavOption(option);
  }

  function SidebarProductEntry(props: any) {
    const option = props.option;
    return <Row >
      <button type="button" className={getOptionStyle(option)} onClick={() => handleOnClick(option)}>
        {option}
      </button>
    </Row>
  }

  return (
    <Container className="sidebar vh-100 d-flex flex-column">
      <Row className="sidebar-app-title">
        Litti Management
      </Row>
        {sidebarProductOptions.map(x => <SidebarProductEntry option={x} key={x}/>)}
    </Container>
  )

}