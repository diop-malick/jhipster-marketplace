import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Payment } from './payment.model';
import { PaymentPopupService } from './payment-popup.service';
import { PaymentService } from './payment.service';
import { MarketOrders, MarketOrdersService } from '../market-orders';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-payment-dialog',
    templateUrl: './payment-dialog.component.html'
})
export class PaymentDialogComponent implements OnInit {

    payment: Payment;
    isSaving: boolean;

    marketorders: MarketOrders[];
    dateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private paymentService: PaymentService,
        private marketOrdersService: MarketOrdersService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.marketOrdersService.query()
            .subscribe((res: ResponseWrapper) => { this.marketorders = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.payment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.paymentService.update(this.payment));
        } else {
            this.subscribeToSaveResponse(
                this.paymentService.create(this.payment));
        }
    }

    private subscribeToSaveResponse(result: Observable<Payment>) {
        result.subscribe((res: Payment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Payment) {
        this.eventManager.broadcast({ name: 'paymentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackMarketOrdersById(index: number, item: MarketOrders) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-payment-popup',
    template: ''
})
export class PaymentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private paymentPopupService: PaymentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.paymentPopupService
                    .open(PaymentDialogComponent as Component, params['id']);
            } else {
                this.paymentPopupService
                    .open(PaymentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
