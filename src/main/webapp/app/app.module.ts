import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { MarketSharedModule, UserRouteAccessService } from './shared';
import { MarketHomeModule } from './home/home.module';
import { MarketAdminModule } from './admin/admin.module';
import { MarketAccountModule } from './account/account.module';
import { MarketEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here
import { AmazonitemModule } from './amazonitem/amazonitem.module'; 
import { AmazonCartModule } from './amazoncart/amazoncart.module'; 

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        MarketSharedModule,
        MarketHomeModule,
        MarketAdminModule,
        MarketAccountModule,
        MarketEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        AmazonitemModule, 
        AmazonCartModule, 
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class MarketAppModule {}
