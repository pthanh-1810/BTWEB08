const API = {
  cat: '/api/v1/categories',
  prod: '/api/v1/products'
};

// ========== CATEGORY ==========
function loadCategories(keyword = '') {
  $.get(`${API.cat}?keyword=${encodeURIComponent(keyword)}&page=0&size=100`, res => {
    if (!res.status) return alert(res.message || 'Load categories failed');
    const list = res.body.content || res.body;
    $('#catTable tbody').html(list.map(x => `
      <tr>
        <td>${x.categoryId}</td>
        <td>${x.categoryName}</td>
        <td>${x.icon ? `<img src="/uploads/${x.icon}" alt="">` : ''}</td>
        <td>
          <button onclick="editCat(${x.categoryId})">Sửa</button>
          <button onclick="delCat(${x.categoryId})" style="background:#ef4444">Xoá</button>
        </td>
      </tr>
    `).join(''));
    $('#prodCategory').html(list.map(x => `<option value="${x.categoryId}">${x.categoryName}</option>`));
  });
}
function editCat(id){
  $.get(`${API.cat}/${id}`, res => {
    if(!res.status) return alert(res.message);
    const c = res.body;
    $('#catId').val(c.categoryId);
    $('#catName').val(c.categoryName);
  }).fail(()=>alert('Không tìm thấy Category'));
}
function delCat(id){
  if(!confirm('Xoá category này?')) return;
  $.ajax({url:`${API.cat}/${id}`, type:'DELETE'}).done(() => {
    loadCategories($('#catSearch').val()||'');
  }).fail(xhr => alert(xhr.responseJSON?.message || 'Xoá thất bại'));
}
$('#btnCatCreate').on('click', () => {
  const fd = new FormData();
  fd.append('categoryName', $('#catName').val());
  const iconFile = $('#catIcon')[0].files[0];
  if (iconFile) fd.append('icon', iconFile);
  $.ajax({url:API.cat, method:'POST', data:fd, processData:false, contentType:false})
    .done(() => { $('#cat-form')[0].reset(); loadCategories(); })
    .fail(xhr => alert(xhr.responseJSON?.message || 'Tạo thất bại'));
});
$('#btnCatUpdate').on('click', () => {
  const id = $('#catId').val();
  if(!id) return alert('Chưa chọn category để cập nhật');
  const fd = new FormData();
  fd.append('categoryName', $('#catName').val());
  const iconFile = $('#catIcon')[0].files[0];
  if (iconFile) fd.append('icon', iconFile);
  $.ajax({url:`${API.cat}/${id}`, method:'PUT', data:fd, processData:false, contentType:false})
    .done(() => { $('#cat-form')[0].reset(); loadCategories(); })
    .fail(xhr => alert(xhr.responseJSON?.message || 'Cập nhật thất bại'));
});
$('#btnCatReload').on('click', () => loadCategories($('#catSearch').val() || ''));

// ========== PRODUCT ==========
function loadProducts(keyword = ''){
  $.get(`${API.prod}?keyword=${encodeURIComponent(keyword)}&page=0&size=100`, res => {
    if (!res.status) return alert(res.message || 'Load products failed');
    const list = res.body.content || res.body;
    $('#prodTable tbody').html(list.map(x => `
      <tr>
        <td>${x.productId}</td>
        <td>${x.productName}</td>
        <td>${x.unitPrice}</td>
        <td>${x.quantity}</td>
        <td>${x.category ? x.category.categoryName : ''}</td>
        <td>${x.images ? `<img src="/uploads/${x.images}" alt="">` : ''}</td>
        <td>
          <button onclick="editProd(${x.productId})">Sửa</button>
          <button onclick="delProd(${x.productId})" style="background:#ef4444">Xoá</button>
        </td>
      </tr>
    `).join(''));
  });
}
function editProd(id){
  $.get(`${API.prod}/${id}`, res => {
    if(!res.status) return alert(res.message);
    const p = res.body;
    $('#prodId').val(p.productId);
    $('#prodName').val(p.productName);
    $('#prodQty').val(p.quantity);
    $('#prodPrice').val(p.unitPrice);
    $('#prodDiscount').val(p.discount);
    $('#prodStatus').val(p.status);
    $('#prodDesc').val(p.description);
    if (p.category) $('#prodCategory').val(p.category.categoryId);
  }).fail(()=>alert('Không tìm thấy Product'));
}
function delProd(id){
  if(!confirm('Xoá product này?')) return;
  $.ajax({url:`${API.prod}/${id}`, type:'DELETE'}).done(() => {
    loadProducts($('#prodSearch').val()||'');
  }).fail(xhr => alert(xhr.responseJSON?.message || 'Xoá thất bại'));
}
$('#btnProdCreate').on('click', () => {
  const fd = new FormData();
  fd.append('productName', $('#prodName').val());
  fd.append('quantity', $('#prodQty').val());
  fd.append('unitPrice', $('#prodPrice').val());
  fd.append('description', $('#prodDesc').val());
  fd.append('discount', $('#prodDiscount').val());
  fd.append('status', $('#prodStatus').val());
  fd.append('categoryId', $('#prodCategory').val());
  const img = $('#prodImage')[0].files[0];
  if (img) fd.append('images', img);
  $.ajax({url:API.prod, method:'POST', data:fd, processData:false, contentType:false})
    .done(() => { $('#prod-form')[0].reset(); loadProducts(); })
    .fail(xhr => alert(xhr.responseJSON?.message || 'Tạo thất bại'));
});
$('#btnProdUpdate').on('click', () => {
  const id = $('#prodId').val();
  if(!id) return alert('Chưa chọn product để cập nhật');
  const fd = new FormData();
  fd.append('productName', $('#prodName').val());
  fd.append('quantity', $('#prodQty').val());
  fd.append('unitPrice', $('#prodPrice').val());
  fd.append('description', $('#prodDesc').val());
  fd.append('discount', $('#prodDiscount').val());
  fd.append('status', $('#prodStatus').val());
  fd.append('categoryId', $('#prodCategory').val());
  const img = $('#prodImage')[0].files[0];
  if (img) fd.append('images', img);
  $.ajax({url:`${API.prod}/${id}`, method:'PUT', data:fd, processData:false, contentType:false})
    .done(() => { $('#prod-form')[0].reset(); loadProducts(); })
    .fail(xhr => alert(xhr.responseJSON?.message || 'Cập nhật thất bại'));
});
$('#btnProdReload').on('click', () => loadProducts($('#prodSearch').val() || ''));

$(function(){
  loadCategories();
  loadProducts();
});
