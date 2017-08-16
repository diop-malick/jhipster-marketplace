import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { MarketProductCategory } from './market-product-category.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MarketProductCategoryService {

    private resourceUrl = 'api/market-product-categories';
    private resourceSearchUrl = 'api/_search/market-product-categories';

    constructor(private http: Http) { }

    create(marketProductCategory: MarketProductCategory): Observable<MarketProductCategory> {
        const copy = this.convert(marketProductCategory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(marketProductCategory: MarketProductCategory): Observable<MarketProductCategory> {
        const copy = this.convert(marketProductCategory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<MarketProductCategory> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(marketProductCategory: MarketProductCategory): MarketProductCategory {
        const copy: MarketProductCategory = Object.assign({}, marketProductCategory);
        return copy;
    }
}