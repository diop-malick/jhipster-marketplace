import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { MarketOrderMySuffix } from './market-order-my-suffix.model';
import { DateUtils } from 'ng-jhipster';

@Injectable()
export class MarketOrderMySuffixService {

    private resourceUrl = 'api/market-orders';
    private resourceSearchUrl = 'api/_search/market-orders';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(marketOrder: MarketOrderMySuffix): Observable<MarketOrderMySuffix> {
        const copy = this.convert(marketOrder);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(marketOrder: MarketOrderMySuffix): Observable<MarketOrderMySuffix> {
        const copy = this.convert(marketOrder);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<MarketOrderMySuffix> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            jsonResponse.orderDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.orderDate);
            jsonResponse.date = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.date);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    private convertResponse(res: Response): Response {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].orderDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse[i].orderDate);
            jsonResponse[i].date = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].date);
        }
        res.json().data = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }

    private convert(marketOrder: MarketOrderMySuffix): MarketOrderMySuffix {
        const copy: MarketOrderMySuffix = Object.assign({}, marketOrder);
        copy.orderDate = this.dateUtils
            .convertLocalDateToServer(marketOrder.orderDate);

        copy.date = this.dateUtils.toDate(marketOrder.date);
        return copy;
    }
}
