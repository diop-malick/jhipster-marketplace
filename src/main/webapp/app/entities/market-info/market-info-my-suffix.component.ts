import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { MarketInfoMySuffix } from './market-info-my-suffix.model';
import { MarketInfoMySuffixService } from './market-info-my-suffix.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-market-info-my-suffix',
    templateUrl: './market-info-my-suffix.component.html'
})
export class MarketInfoMySuffixComponent implements OnInit, OnDestroy {
marketInfos: MarketInfoMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private marketInfoService: MarketInfoMySuffixService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.marketInfoService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: Response) => this.marketInfos = res.json(),
                    (res: Response) => this.onError(res.json())
                );
            return;
       }
        this.marketInfoService.query().subscribe(
            (res: Response) => {
                this.marketInfos = res.json();
                this.currentSearch = '';
            },
            (res: Response) => this.onError(res.json())
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInMarketInfos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: MarketInfoMySuffix) {
        return item.id;
    }
    registerChangeInMarketInfos() {
        this.eventSubscriber = this.eventManager.subscribe('marketInfoListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
