import { Injectable } from '@angular/core';
import { HeaderComponent } from '../../component/navigation/header/header.component';
import {UtilService} from "../../tool/util.service";

@Injectable({
  providedIn: 'root'
})
export class HeaderService {
    private _headerComponent: HeaderComponent | undefined;

    setHeaderComponent(headerComponent: HeaderComponent): void {
        this._headerComponent = headerComponent;
    }

    isSmallScreen(): boolean {
        if (this._headerComponent) {
            return this._headerComponent.isSmallScreen();
        }
        return false;
    }
}
