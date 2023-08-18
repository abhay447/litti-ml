"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import '@/app/styles/entity.css';

export const OPTION_ADD = "add";
export const OPTION_LIST = "list";

export default function ModelMenu(props: any) {

  const setSelectedOption = props.setSelectedOption;
  const selectedOption = props.selectedOption;

  function handleOnClick(option: string) {
    setSelectedOption(option);
  }

  function getOptionStyle(option: string) {
    if(option === selectedOption) {
      return "entity-top-menu-col entity-top-menu-col-selected";
    }
    return "entity-top-menu-col";
  }

  return (
    <Row>
      <Col style={{ display: 'flex', justifyContent: 'center' }} className={getOptionStyle(OPTION_LIST)}>
        <button type="button" onClick={() => handleOnClick(OPTION_LIST)}>List Models</button>
      </Col>
      <Col style={{ display: 'flex', justifyContent: 'center' }} className={getOptionStyle(OPTION_ADD)}>
        <button type="button" onClick={() => handleOnClick(OPTION_ADD)}>Add Model</button>
      </Col>
    </Row>
  )

}
