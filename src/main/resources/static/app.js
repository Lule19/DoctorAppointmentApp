function pad2(n){return n<10?'0'+n:n}
function toIsoLocal(dtValue){
  if(!dtValue) return null;
  // datetime-local format: YYYY-MM-DDTHH:mm; 
  if(dtValue.length===16){return dtValue+':00'}
  return dtValue
}

async function fetchJson(url){
  const res = await fetch(url)
  if(!res.ok) throw new Error('HTTP '+res.status)
  return await res.json()
}

async function loadPatients(){
  const sel = document.getElementById('patient')
  const data = await fetchJson('/patients')
  sel.innerHTML = data.map(p=>`<option value="${p.id}">${p.id} - ${p.firstName} ${p.lastName}</option>`).join('')
}

async function loadDoctors(){
  const sel1 = document.getElementById('doctor')
  const sel2 = document.getElementById('fDoctor')
  const data = await fetchJson('/doctors')
  sel1.innerHTML = data.map(d=>`<option value="${d.id}">${d.id} - ${d.firstName} ${d.lastName} (${d.specialization||''})</option>`).join('')
  sel2.innerHTML = '<option value="">(svi)</option>'+data.map(d=>`<option value="${d.id}">${d.id} - ${d.firstName} ${d.lastName}</option>`).join('')
}

async function createAppointment(){
  const payload = {
    patientId: Number(document.getElementById('patient').value),
    doctorId: Number(document.getElementById('doctor').value),
    startTime: toIsoLocal(document.getElementById('start').value),
    endTime: toIsoLocal(document.getElementById('end').value),
    reason: document.getElementById('reason').value || 'Pregled'
  }
  const res = await fetch('/appointments', {method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(payload)})
  const out = document.getElementById('createResult')
  if(res.ok){
    const appt = await res.json()
    out.textContent = `Kreiran termin #${appt.id} status=${appt.status}`
    await loadAppointments()
  }else{
    const err = await res.json().catch(()=>({message:'Greška'}))
    out.textContent = 'Greška: '+ (err.message || JSON.stringify(err))
  }
}

async function loadAppointments(){
  const params = new URLSearchParams()
  const d = document.getElementById('fDoctor').value
  const s = document.getElementById('fStatus').value
  const f = toIsoLocal(document.getElementById('fFrom').value)
  const t = toIsoLocal(document.getElementById('fTo').value)
  if(d) params.append('doctorId', d)
  if(s) params.append('status', s)
  if(f) params.append('from', f)
  if(t) params.append('to', t)
  const list = await fetchJson('/appointments'+(params.toString()?('?'+params.toString()):''))
  const root = document.getElementById('list')
  const rows = list.map(a=>`<tr><td>${a.id}</td><td>${a.patient? (a.patient.firstName+' '+a.patient.lastName):a.patientId||''}</td><td>${a.doctor? (a.doctor.firstName+' '+a.doctor.lastName):a.doctorId||''}</td><td>${a.startTime||''}</td><td>${a.endTime||''}</td><td>${a.status}</td><td>${a.reason||''}</td></tr>`).join('')
  root.innerHTML = `<table><thead><tr><th>ID</th><th>Pacijent</th><th>Lekar</th><th>Početak</th><th>Kraj</th><th>Status</th><th>Razlog</th></tr></thead><tbody>${rows}</tbody></table>`
}



window.addEventListener('DOMContentLoaded', async ()=>{
  try{
    await Promise.all([loadPatients(), loadDoctors()])
    document.getElementById('createBtn').addEventListener('click', createAppointment)
    document.getElementById('loadBtn').addEventListener('click', loadAppointments)
    await loadAppointments()
  // Auto-refresh appointments every few seconds (kept)
  setInterval(loadAppointments, 3000)
  }catch(e){ console.error(e) }
})
