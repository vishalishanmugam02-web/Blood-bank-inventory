async function getStock(){const r=await fetch('/api/inventory/stock');if(r.status===401){location.href='/';return null}return r.json()}
function renderStock(data){const el=document.getElementById('stockCards');if(!el||!data)return;el.innerHTML=Object.entries(data).map(([g,n])=>`<div class="stock"><strong>${n}</strong>${g.replace('_',' ')}</div>`).join('')}
async function logout(){await fetch('/api/auth/logout',{method:'POST'});location.href='/'}
