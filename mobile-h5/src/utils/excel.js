import * as XLSX from 'xlsx'

export function exportToExcel(data, columns, filename = 'export.xlsx') {
  const headers = columns.map(col => col.title)
  const rows = data.map(row => columns.map(col => row[col.key] || ''))
  
  const worksheet = XLSX.utils.aoa_to_sheet([headers, ...rows])
  
  const workbook = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1')
  
  XLSX.writeFile(workbook, filename)
}

export function readExcelFile(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      try {
        const data = new Uint8Array(e.target.result)
        const workbook = XLSX.read(data, { type: 'array' })
        const sheetName = workbook.SheetNames[0]
        const worksheet = workbook.Sheets[sheetName]
        const jsonData = XLSX.utils.sheet_to_json(worksheet)
        resolve(jsonData)
      } catch (error) {
        reject(error)
      }
    }
    reader.onerror = reject
    reader.readAsArrayBuffer(file)
  })
}

export function formatExcelData(data, columns) {
  return data.map(row => {
    const formattedRow = {}
    columns.forEach(col => {
      if (col.formatter) {
        formattedRow[col.key] = col.formatter(row[col.key], row)
      } else {
        formattedRow[col.key] = row[col.key] || ''
      }
    })
    return formattedRow
  })
}