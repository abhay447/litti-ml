"use client";
import { ModelEntity } from "@/app/entities/modelEntity";
import { useEffect, useState } from "react"
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import { DataGrid } from "@mui/x-data-grid";


const modelTableColumns = [
  { field: 'name', headerName: 'Name' , width:150},
  { field: 'version', headerName: 'Version' , width:150},
  { field: 'modelFramework', headerName: 'Model Framework' , width:150},
  { field: 'domain', headerName: 'Domain' , width:150},
  { field: 'outputs', headerName: 'Outputs' , width:400},
]


// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function ModelListComponent() {

  const [data, setData] = useState(null)
  const [isLoading, setLoading] = useState(true)
 
  useEffect(() => {
    fetch('http://localhost:8081/models')
      .then((res) => res.json())
      .then((data) => {
        setData(data)
        setLoading(false)
      })
  }, [])
 
  if (isLoading) return <p>Loading...</p>
  if (!data) return <p>No profile data</p>

  const models = (data as Array<any>).map(
    entry => new ModelEntity(
      entry.name,
      entry.version,
      entry.domain,
      entry.modelFramework,
      entry.modelLocation,
      entry.outputs,
      entry.id
    )
  )
 
  return (
    <DataGrid rows={models} columns={modelTableColumns}/>
  )

}