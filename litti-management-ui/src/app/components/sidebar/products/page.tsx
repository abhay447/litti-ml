"use client";
import { Container, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/styles/sidebar.css'

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export const NAV_OPTION_MODELS = "Models";
export const NAV_OPTION_FEATURES = "Features";
export const NAV_OPTION_MODEL_CLUSTERS = "Model Clusters";
export const NAV_OPTION_FEATURE_STORES = "Feature Stores";

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

  return (
    <Container className="sidebar vh-100 d-flex flex-column">
      <Row className="sidebar-app-title">
        Litti Management
      </Row>
      <Row>
        <button type="button" className={getOptionStyle(NAV_OPTION_MODELS)} onClick={() => handleOnClick(NAV_OPTION_MODELS)}>
          {NAV_OPTION_MODELS}
        </button>
      </Row>
      <Row>
        <button type="button" className={getOptionStyle(NAV_OPTION_FEATURES)} onClick={() => handleOnClick(NAV_OPTION_FEATURES)}>
          {NAV_OPTION_FEATURES}
        </button>
      </Row>
      <Row>
        <button type="button" className={getOptionStyle(NAV_OPTION_MODEL_CLUSTERS)} onClick={() => handleOnClick(NAV_OPTION_MODEL_CLUSTERS)}>
          {NAV_OPTION_MODEL_CLUSTERS}
        </button>
      </Row>
      <Row>
        <button type="button" className={getOptionStyle(NAV_OPTION_FEATURE_STORES)} onClick={() => handleOnClick(NAV_OPTION_FEATURE_STORES)}>
          {NAV_OPTION_FEATURE_STORES}
        </button>
      </Row>
    </Container>
  )

}