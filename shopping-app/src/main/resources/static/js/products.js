/**
 * 
 */

// 現在のページのURLからクエリパラメータを取得
const url = new URL(window.location.href);

const apiUrl = 'http://localhost:8080/api/products';

let select = document.querySelector('[name="sort"]');
select.onchange = event => {
  productIndex(apiUrl + url.search + "&sort=" + select.value);
}

productIndex(apiUrl + url.search);

// apiからデータ取得
function productIndex(url) {
	fetch(url)
	  .then((response) => response.json())
	  .then((json) => {
	//	  console.log(json)
		  // データを元にHTMLを生成
	      const productsHtml = json.map(product => createProductCard(product)).join('');
	      // DOMに追加
	      document.querySelector('#productList').innerHTML = productsHtml;
	      
	      console.log(json.length);
	  })
	  .catch((error) => {
		  console.error('データの取得中にエラーが発生しました:', error);
	  })
}

// 商品一覧出力
function createProductCard(product) {
	let token = document.querySelector("meta[name='_csrf']").getAttribute("content");
	return `
	<div class="card d-block m-2 p-0" style="width: 180px;">
	  <a href="/products/${product.id}">
	    <img src="${product.imagePath}" style="height: 100px;" onerror="this.src='/images/no_image.png';" class="card-img-top">
	  </a>
	  <div class="card-body">
	    <a href="/products/${product.id}">
	      <h5 class="card-name">${product.name}</h5>
	    </a>
	    <p class="card-description">￥${product.price.toLocaleString()}</p>
	    ${product.wishUserId !== userAccountId ?
	      `<a href="javascript:save${product.id}.submit()">♡</a>
	      <form action="/wish_products/${product.id}" method="post" name="save${product.id}">
	        <input type="hidden" name="_csrf" value="${token}" />
	      </form>`
	      :
	      `<a href="javascript:delete${product.id}.submit()">♥</a>
	      <form action="/wish_products/${product.id}" method="post" name="delete${product.id}">
	        <input type="hidden" name="_csrf" value="${token}" />
	        <input type="hidden" name="_method" value="delete" />
	      </form>`
	    }
	  </div>
	</div>
	`;
}

// ページング出力
//function paging() {
//	let totalData = 25;
//	let currentPage = 1;
//	let step = 15;
//	let totalPage = Math.floor(totalData / step) + 1;
//	
//	
//	
//	return `
//	${currentPage > 1 && `<button class="page-link" value="${currentPage - 1}">前へ</button>`}
//	${currentPage < totalPage && `<button class="page-link" value="${currentPage + 1}"`}
//	`
//}



