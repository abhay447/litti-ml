"use client";
import { ModelEntity } from "@/app/entities/modelEntity";
import { useEffect, useState } from "react"
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import { DataGrid } from "@mui/x-data-grid";
import { getModels } from "@/app/services/modelService";


const modelTableColumns = [
  { field: 'name', headerName: 'Name' , width:150},
  { field: 'version', headerName: 'Version' , width:150},
  { field: 'modelFramework', headerName: 'Model Framework' , width:150},
  { field: 'domain', headerName: 'Domain' , width:150},
  { field: 'outputs', headerName: 'Outputs' , width:400},
]


// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function ModelListComponent() {

  const [models, setModels] = useState<ModelEntity[]>()
  const [isLoading, setLoading] = useState(true)
 
  useEffect(() => {

    getModels()
      .then((data) => {
        setModels(data)
        setLoading(false)
      })
  }, []);
 
  if (isLoading) return <p>Loading...</p>
  if (!models) return <p>No profile data</p>

 
  return (
    <DataGrid rows={models} columns={modelTableColumns}/>
  )

}